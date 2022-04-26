import { IProduct } from 'app/shared/model/product.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IProductCategory {
  id?: number;
  name?: string;
  description?: string | null;
  products?: IProduct[] | null;
  category?: ICategory;
}

export const defaultValue: Readonly<IProductCategory> = {};
