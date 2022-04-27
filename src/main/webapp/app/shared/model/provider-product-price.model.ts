import { IPrice } from 'app/shared/model/price.model';

export interface IProviderProductPrice {
  id?: number;
  name?: string;
  value?: number;
  price?: IPrice | null;
}

export const defaultValue: Readonly<IProviderProductPrice> = {};
