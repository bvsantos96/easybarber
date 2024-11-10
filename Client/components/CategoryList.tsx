import { useEffect, useRef, useState } from "react";
import { View } from "react-native";
import PagerView from "react-native-pager-view";
import { SvgUri } from "react-native-svg";

import { getStyles as topBarGetStyles } from '../styles/TopBar';
import { getStyles as getHomeGetStyles } from '../styles/Home';
import Category from "./Category";
import { useTheme } from "@styles/ThemeContext";

interface CategoriesListProps {
    categories: ICategory[];
    maxWidth: number;
    filter?: IFilterRequest;
    setFilter?: (_filter: IFilterRequest) => void;
    categorySize?: number;
    padding?: number;
}

export default function CategoriesList({ categories, maxWidth, filter, setFilter, categorySize = 60, padding = 10 }: CategoriesListProps) {
    const theme = useTheme();
    const homeStyles = getHomeGetStyles();
    const topBarStyles = topBarGetStyles();

    const pagerRef = useRef<PagerView>(null);
    const [categoryRows, setCategoryRows] = useState<ICategory[][]>([]);
    const _padding = useRef(padding);

    useEffect(() => {
        const elementsPerPage = Math.floor(maxWidth / (categorySize + (padding * 2)) * theme.dimensions.absoluteWidth);
        const rows = [];
        for (let i = 0; i < categories.length; i += elementsPerPage) {
            rows.push(categories.slice(i, i + elementsPerPage));
        }
        _padding.current = (maxWidth - (elementsPerPage * categorySize)) / (elementsPerPage * 2);
        setCategoryRows(rows);
    }, [categories]);

    if (categoryRows.length === 0) {
        return <></>;
    }

    return (
        <PagerView
            ref={pagerRef}
            style={{ justifyContent: "center", alignItems: "center", minHeight: "100%", minWidth: maxWidth }}>
            {categoryRows.map((_categories: ICategory[], index) => (
                <View style={{ minHeight: (categorySize + 20) * theme.dimensions.absoluteHeight, flexDirection: 'row', alignItems: "center" }} key={index}>
                    {_categories && _categories.map((category: ICategory) => (
                        <View style={{ paddingHorizontal: _padding.current }} key={category.id}>
                            <Category
                                style={{ maxWidth: categorySize * theme.dimensions.absoluteWidth }}
                                size={categorySize}
                                key={category.id}
                                id={category.id}
                                padding={1.5}
                                icon={
                                    category.imageURL && category.imageURL.length > 0 &&
                                    (<SvgUri width={topBarStyles.categoryIcon.width}
                                        height={topBarStyles.categoryIcon.height}
                                        style={homeStyles.alignCenter}
                                        uri={category.imageURL} />)}
                                title={category.name}
                                select={setFilter}
                                selectedCategory={
                                    filter && typeof filter === 'object'
                                        && 'serviceType' in filter
                                        && typeof filter.serviceType === 'string'
                                        ? parseInt(filter.serviceType)
                                        : -1
                                }
                            />
                        </View>
                    )
                    )}
                </View>
            ))}
        </PagerView >
    );
}
