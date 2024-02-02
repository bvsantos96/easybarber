module.exports = ((api) => {
    api.cache(true);

    const config = getDefaultConfig(__dirname);

    const { transformer, resolver } = config;

    config.transformer = {
        ...transformer,
        babelTransformerPath: require.resolve("react-native-svg-transformer")
    };
    config.resolver = {
        ...resolver,
        assetExts: resolver.assetExts.filter((ext) => ext !== "svg"),
        sourceExts: [...resolver.sourceExts, "svg"]
    };
    config.presets = ['babel-preset-expo'];
    config.env =  { 
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
    return config;
});
