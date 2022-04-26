import { IProduct } from 'app/shared/model/product.model';
import { IProvider } from 'app/shared/model/provider.model';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';

export interface IProviderProduct {
  id?: number;
  name?: string;
  description?: string | null;
  products?: IProduct[] | null;
  provider?: IProvider;
  providerProductPrice?: IProviderProductPrice;
}

export const defaultValue: Readonly<IProviderProduct> = {};
