export const createPageable = <T>(size = 10): IPage<T> => {
    return {
        content: [],
        totalPages: 0,
        totalElements: 0,
        currentPage: 0,
        pageSize: size,
        hasNextPage: true,
        hasPreviousPage: false
    };
}

export const parsePage = <T>(page: Pageable<T>): IPage<T> => {
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

export const pageToRequest = (page: IPage<any>): IPage<any> => {
    return {
        content: [],
        totalPages: page.totalPages,
        totalElements: page.totalElements,
        currentPage: page.currentPage,
        pageSize: page.pageSize,
        hasNextPage: page.hasNextPage,
        hasPreviousPage: page.hasPreviousPage
    }
}
