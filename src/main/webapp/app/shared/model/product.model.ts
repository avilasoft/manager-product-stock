import { IUnit } from 'app/shared/model/unit.model';
import { IProductCategory } from 'app/shared/model/product-category.model';

export interface IProduct {
  id?: number;
  item?: string;
  name?: string;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  quantity?: number | null;
  unit?: IUnit;
  productCategory?: IProductCategory;
}

export const defaultValue: Readonly<IProduct> = {};
