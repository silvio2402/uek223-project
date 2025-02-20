class RefreshTokenNotFoundException extends Error {
  constructor() {
    super("Refresh token cookie not found.")
    this.name = "RefreshTokenNotFoundException"
    Object.setPrototypeOf(this, RefreshTokenNotFoundException.prototype)
  }
}

export default RefreshTokenNotFoundException
