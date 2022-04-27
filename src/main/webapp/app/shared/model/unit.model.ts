import { IProduct } from 'app/shared/model/product.model';
import { IPrice } from 'app/shared/model/price.model';
import { IUnitType } from 'app/shared/model/unit-type.model';

export interface IUnit {
  id?: number;
  name?: string;
  description?: string;
  symbol?: string;
  isBase?: boolean | null;
  products?: IProduct[] | null;
  prices?: IPrice[] | null;
  unitType?: IUnitType;
}

export const defaultValue: Readonly<IUnit> = {
  isBase: false,
};
