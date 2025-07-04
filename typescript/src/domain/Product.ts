import Category from './Category';

class Product {
  private name: string;
  private price: number;
  private category: Category;

  public getName(): string {
    return this.name;
  }

  public setName(name: string): void {
    this.name = name;
  }

  public getPrice(): number {
    return this.price;
  }

  public setPrice(price: number): void {
    this.price = price;
  }

  public getCategory(): Category {
    return this.category;
  }

  public setCategory(category: Category): void {
    this.category = category;
  }

  public static calculateUnitaryTax(product: Product): number {
    return Math.round(product.getPrice() / 100 * product.getCategory().getTaxPercentage() * 100) / 100;
  }

  public static calculateUnitaryTaxedAmount(product: Product, unitaryTax: number): number {
    return Math.round((product.getPrice() + unitaryTax) * 100) / 100;
  }
}

export default Product;

