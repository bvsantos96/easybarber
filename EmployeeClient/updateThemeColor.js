const fs = require('fs');
const path = require('path');

let gitignorePatterns = [];

function loadGitignore() {
    try {
        const gitignorePath = path.join(process.cwd(), '.gitignore');
        if (fs.existsSync(gitignorePath)) {
            const gitignoreContent = fs.readFileSync(gitignorePath, 'utf8');
            gitignorePatterns = gitignoreContent
                .split('\n')
                .map((line) => line.trim())
                .filter((line) => line && !line.startsWith('#'));
        }
    } catch (error) {
        console.error('Error loading .gitignore:', error.message);
    }
}


function isIgnored(filePath) {
    const relativePath = path.relative(process.cwd(), filePath);
    return gitignorePatterns.some((pattern) => {
        if (pattern.endsWith('/')) {

            return relativePath.startsWith(pattern.slice(0, -1));
        }
        return relativePath === pattern;
    });
}

function replaceInFile(filePath, searchValue, replaceValue) {
    if (isIgnored(filePath)) {
        return;
    }

    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const updatedContent = content.replace(new RegExp(searchValue, 'g'), replaceValue);
        fs.writeFileSync(filePath, updatedContent, 'utf8');
        console.log(`Updated: ${filePath}`);
    } catch (error) {
        console.error(`Failed to process ${filePath}: ${error.message}`);
    }
}

function processDirectory(directory, searchValue, replaceValue) {
    fs.readdirSync(directory).forEach((entry) => {
        const entryPath = path.join(directory, entry);

        if (isIgnored(entryPath)) {
            return;
        }

        if (fs.statSync(entryPath).isDirectory()) {
            processDirectory(entryPath, searchValue, replaceValue);
        } else if (fs.statSync(entryPath).isFile()) {
            replaceInFile(entryPath, searchValue, replaceValue);
        }
    });
}

function extractMainColor(themeFilePath) {
    try {
        const fileContent = fs.readFileSync(themeFilePath, 'utf8');
        const match = fileContent.match(/const\s+mainColor\s*=\s*['"`](.*?)['"`]/);

        if (match && match[1]) {
            return match[1];
        } else {
            throw new Error('mainColor not found in the file.');
        }
    } catch (error) {
        console.error(`Error reading the file: ${error.message}`);
        return null;
    }
}

const [replaceValue] = process.argv.slice(2);

if (!replaceValue) {
    console.error('This script runs like this: node updateThemeColor.js <new-color>');
    process.exit(1);
}

loadGitignore();

const searchValue = extractMainColor('./styles/ThemeContext.tsx');

if (!searchValue) {
    console.error('Could not determine the current mainColor.');
    process.exit(1);
}

processDirectory(process.cwd(), searchValue, replaceValue);
