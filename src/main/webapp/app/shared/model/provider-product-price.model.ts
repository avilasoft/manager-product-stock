import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { IPrice } from 'app/shared/model/price.model';
import { IUnit } from 'app/shared/model/unit.model';

export interface IProviderProductPrice {
  id?: number;
  name?: string;
  value?: number;
  providerProducts?: IProviderProduct[] | null;
  price?: IPrice | null;
  unit?: IUnit;
}

export const defaultValue: Readonly<IProviderProductPrice> = {};
