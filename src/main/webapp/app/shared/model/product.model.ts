import { IUnit } from 'app/shared/model/unit.model';
import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { IBatchProduct } from 'app/shared/model/batch-product.model';

export interface IProduct {
  id?: number;
  item?: string;
  name?: string;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  quantity?: number | null;
  unit?: IUnit;
  providerProduct?: IProviderProduct;
  productCategory?: IProductCategory;
  batchProducts?: IBatchProduct[] | null;
}

export const defaultValue: Readonly<IProduct> = {};
