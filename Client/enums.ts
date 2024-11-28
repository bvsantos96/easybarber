export enum UpdateType {
    MAJOR = "major",
    MINOR = "minor",
    PATCH = "patch",
    NONE = "none",
    FAILED = "failed"
}

export enum ResponseType {
    NONE = -1,
    STRING = 0,
    OBJECT = 1,
    LIST = 2,
    FULL_LIST = 3
}

export enum PageListType {
    FLAT = "flat",
    BOTTOM_SHEET = "bottom_sheet",
    PAGERVIEW = "page_view"
}

export enum MobileConfirmationFunctions {
    REGISTER = "register",
}
