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
}
