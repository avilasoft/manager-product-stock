import { IProduct } from 'app/shared/model/product.model';
import { IBatchProduct } from 'app/shared/model/batch-product.model';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { IProvider } from 'app/shared/model/provider.model';

export interface IProviderProduct {
  id?: number;
  name?: string;
  description?: string | null;
  product?: IProduct | null;
  batchProduct?: IBatchProduct | null;
  providerProductPrice?: IProviderProductPrice | null;
  provider?: IProvider;
}

export const defaultValue: Readonly<IProviderProduct> = {};
