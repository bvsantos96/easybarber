export interface Pageable<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            sorted: boolean;
            unsorted: boolean;
        },
        offset: number;
        paged: boolean;
        unpaged: boolean;
    },
    last: boolean;
    totalElements: number;
    totalPages: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    },
    first: boolean;
    size: number;
    number: number;
    numberOfElements: number;
    empty: boolean;
}
    
export const createPageable = <T>(): Page<T> => {
    return {
        content: [],
        totalPages: 0,
        totalElements: 0,
        currentPage: 0,
        pageSize: 10,
        hasNextPage: true,
        hasPreviousPage: false
    };
}

export const parsePage = <T>(page: Pageable<T>): Page<T> => {
    return {
        content: page.content,
        totalPages: page.totalPages,
        totalElements: page.totalElements,
        currentPage: page.pageable.pageNumber + 1,
        pageSize: page.pageable.pageSize,
        hasNextPage: !page.last,
        hasPreviousPage: !page.first
    }
}
