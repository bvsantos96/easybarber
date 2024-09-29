module.exports = function(api) {
    api.cache(true);
    return {
        presets: ['babel-preset-expo'],
        plugins: [
            ['module:react-native-dotenv', {
                moduleName: '@env',
                path: '.env',
                blacklist: null,
                whitelist: null,
                safe: false,
                allowUndefined: true,
            }],
            [
                'module-resolver',
                {
                    root: ['./'],
                    alias: {
                        "@components": "./components",
                        "@assets": "./assets",
                        "@screens": "./screens",
                        "@styles": "./styles",
                        "@icons": "./components/icons",
                        "@lang": "./langs",
                    },
                    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json', '.svg'],
                }
            ],
            '@babel/plugin-proposal-export-namespace-from',
            'react-native-reanimated/plugin',
        ],
        env: {
            development: {
                plugins: [
                    // If you need any specific plugins for development, you can add them here
                ],
            }
        }
    };
};
