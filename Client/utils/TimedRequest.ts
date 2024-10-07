export class TimedRequest<T extends Identifiable> implements ITimedRequest<T> {
    page: IPage<T>;
    lastRequest: number;
    loadingMore?: boolean | undefined;
    pathParams?: {};

    constructor(page: IPage<T>, lastRequest: number, pathParams?: {}) {
        this.page = page;
        this.lastRequest = lastRequest;
        this.loadingMore = false;
        this.pathParams = pathParams;
    }

    pageToRequest(): IPage<T> {
        return {
            content: [],
            totalPages: this.page.totalPages,
            totalElements: this.page.totalElements,
            currentPage: this.page.currentPage,
            pageSize: this.page.pageSize,
            hasNextPage: this.page.hasNextPage,
            hasPreviousPage: this.page.hasPreviousPage
        }
    }

    combineUniqueItems(items: T[], newItems: T[]): T[] {
        let uniqueItems = new Set<T>();
        items.forEach(item => uniqueItems.add(item));
        newItems.forEach(item => uniqueItems.add(item));
        return Array.from(uniqueItems);
    }

    handlePage(page: IPage<T>) {
        this.page.content = this.combineUniqueItems(this.page.content, page.content);
        this.page.totalPages = page.totalPages;
        this.page.currentPage++;
        this.page.pageSize = page.pageSize;
        this.page.hasNextPage = page.hasNextPage;
        this.page.hasPreviousPage = page.hasPreviousPage;
    }

    async request(func: (page: IPage<T>, params: Record<string, string | number | boolean> | undefined) => Promise<IPage<T> | undefined>): Promise<boolean> {
        // TODO: Test 5000ms
        if (!this.page.hasNextPage || this.loadingMore || Date.now() - this.lastRequest < 5000) {
            return false;
        }
        try {
            this.loadingMore = true;
            const result = await func(this.pageToRequest(), this.pathParams);
            this.loadingMore = false;
            if (!result || !result.content || result.content.length === 0) {
                this.lastRequest = Date.now();
                return false;
            }
            this.lastRequest = 0;
            this.handlePage(result);
            return true;
        }
        catch (error) {
            this.lastRequest = Date.now();
            console.error('Error loading more items:', error);
            return false;
        }
    }
}
