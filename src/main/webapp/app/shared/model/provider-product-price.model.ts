import { IProviderProduct } from 'app/shared/model/provider-product.model';
import { IUnit } from 'app/shared/model/unit.model';

export interface IProviderProductPrice {
  id?: number;
  price?: number | null;
  providerProduct?: IProviderProduct;
  units?: IUnit[];
}

export const defaultValue: Readonly<IProviderProductPrice> = {};
