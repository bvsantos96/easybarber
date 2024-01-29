module.exports = function(api) {
    api.cache(true);
    return {
        presets: ['babel-preset-expo'],
        env: {
            development: {
                plugins: [
                    [
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
                    'react-native-reanimated/plugin',
                ],
            }
        }
    };
};
