export class CustomerLoginDto {
  constructor(
    private email: string,
    private password: string
  ) {}

  public getEmail() {
    return this.email;
  }

  public getPassword() {
    return this.password;
  }
}
