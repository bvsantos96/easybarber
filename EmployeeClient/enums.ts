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
    FULL_LIST = 3,
    VALUE = 4
}

export enum PageListType {
    FLAT = "flat",
    BOTTOM_SHEET = "bottom_sheet",
    PAGERVIEW = "page_view",
    MULTI_COL_LIST = "multi_col_list"
}

export enum MobileConfirmationFunctions {
    REGISTER = "register",
    CONFIRMATION_CODE = "confirmation_code",
    RESET_PASSWORD = "reset_password",
}

export enum DayOccupancyType {
    FREE = 0,
    AVAILABLE = 1,
    MEDIUM = 2,
    FULL = 3
}
