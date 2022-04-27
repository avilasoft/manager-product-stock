import { IProvider } from 'app/shared/model/provider.model';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IProviderProduct {
  id?: number;
  name?: string;
  description?: string | null;
  provider?: IProvider;
  providerProductPrice?: IProviderProductPrice;
  product?: IProduct;
}

export const defaultValue: Readonly<IProviderProduct> = {};
