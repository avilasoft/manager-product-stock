import { IUnitType } from 'app/shared/model/unit-type.model';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IUnit {
  id?: number;
  name?: string;
  description?: string;
  symbol?: string;
  isBase?: boolean | null;
  unitType?: IUnitType | null;
  providerProductPrice?: IProviderProductPrice | null;
  parents?: IUnit[] | null;
  product?: IProduct | null;
  children?: IUnit[] | null;
}

export const defaultValue: Readonly<IUnit> = {
  isBase: false,
};
