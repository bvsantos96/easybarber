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
    const ignoredExtensions = ['otf', 'ttf', 'png', 'gif'];
    const fileExtension = path.extname(filePath).slice(1);

    if (ignoredExtensions.includes(fileExtension)) {
        return;
    }

    if (isIgnored(filePath)) {
        return;
    }

    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const updatedContent = content.replace(new RegExp(searchValue, 'gi'), replaceValue);
        fs.writeFileSync(filePath, updatedContent, 'utf8');
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

function isValidHexColor(str) {
    const hexRegex = /^#([0-9A-Fa-f]{3}){1,2}([0-9A-Fa-f]{2})?$|^#([0-9A-Fa-f]{6})([0-9A-Fa-f]{2})?$/;

    return hexRegex.test(str);
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

function hexToRgbValues(hex) {
    hex = hex.replace(/^#/, '');

    if (hex.length === 3) {
        hex = hex.split('').map(char => char + char).join('');
    }

    const r = parseInt(hex.slice(0, 2), 16);
    const g = parseInt(hex.slice(2, 4), 16);
    const b = parseInt(hex.slice(4, 6), 16);

    return `${r}, ${g}, ${b}`;
}

function hexToRgb(hex) {
    hex = hex.replace(/^#/, '');

    if (hex.length === 3) {
        hex = hex.split('').map(char => char + char).join('');
    }

    const r = parseInt(hex.slice(0, 2), 16);
    const g = parseInt(hex.slice(2, 4), 16);
    const b = parseInt(hex.slice(4, 6), 16);

    return `rgba(${r}, ${g}, ${b}, 0.1)`;
}

function rgbToHex(r, g, b) {
    r = Math.max(0, Math.min(255, +r));
    g = Math.max(0, Math.min(255, +g));
    b = Math.max(0, Math.min(255, +b));

    const toHex = (n) => n.toString(16).padStart(2, '0');

    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

let [replaceValue] = process.argv.slice(2);

if (!replaceValue) {
    console.error('This script runs like this: node updateThemeColor.js <new-color>');
    process.exit(1);
}

if (!isValidHexColor(replaceValue)) {
    const [r, g, b] = replaceValue.split(', ');
    replaceValue = rgbToHex(r, g, b);
}

loadGitignore();

let searchValue = extractMainColor('./styles/ThemeContext.tsx');

if (!searchValue) {
    console.error('Could not determine the current mainColor.');
    process.exit(1);
}

if (!isValidHexColor(searchValue)) {
    const [r, g, b] = searchValue.split(', ');
    searchValue = rgbToHex(r, g, b);
}

processDirectory(process.cwd(), searchValue, replaceValue);
processDirectory(process.cwd(), hexToRgbValues(searchValue), hexToRgbValues(replaceValue));
