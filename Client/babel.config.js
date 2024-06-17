module.exports = function(api) {
    api.cache(true);
    return {
        presets: ['babel-preset-expo'],
        env: {
            development: {
                plugins: [
                    ['module:react-native-dotenv', {
                        moduleName: '@env',
                        path: '.env',
                        blacklist: null,
                        whitelist: null,
                        safe: false,
                        allowUndefined: true,
                    }], [
                        'module-resolver',
                        {
                            alias: {
                                "@components": "./components",
                                "@assets": "./assets",
                                "@screens": "./screens",
                                "@styles": "./styles",
                                "@icons": "./components/icons",
                                "@lang": "./langs",
                            },
                        }
                    ],
                    '@babel/plugin-proposal-export-namespace-from',
                    'react-native-reanimated/plugin',
                ],
            }
        }
    };
};
