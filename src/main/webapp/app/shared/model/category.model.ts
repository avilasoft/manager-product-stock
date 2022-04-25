import { IProductCategory } from 'app/shared/model/product-category.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string | null;
  productCategories?: IProductCategory[] | null;
}

export const defaultValue: Readonly<ICategory> = {};
