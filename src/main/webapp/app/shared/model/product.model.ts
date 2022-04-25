import { IUnit } from 'app/shared/model/unit.model';
import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { IProductCategory } from 'app/shared/model/product-category.model';

export interface IProduct {
  id?: number;
  item?: string;
  name?: string;
  description?: string | null;
  quantity?: number | null;
  imageContentType?: string | null;
  image?: string | null;
  units?: IUnit[];
  providerProducts?: IProviderProduct[];
  productCategories?: IProductCategory[];
}

export const defaultValue: Readonly<IProduct> = {};
