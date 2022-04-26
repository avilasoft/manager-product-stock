import { IProduct } from 'app/shared/model/product.model';
import { IProviderProductPrice } from 'app/shared/model/provider-product-price.model';
import { IUnitEquivalence } from 'app/shared/model/unit-equivalence.model';
import { IUnitType } from 'app/shared/model/unit-type.model';

export interface IUnit {
  id?: number;
  name?: string;
  description?: string;
  symbol?: string;
  isBase?: boolean | null;
  products?: IProduct[] | null;
  providerProductPrices?: IProviderProductPrice[] | null;
  unitEquivalences?: IUnitEquivalence[] | null;
  unitType?: IUnitType;
}

export const defaultValue: Readonly<IUnit> = {
  isBase: false,
};
