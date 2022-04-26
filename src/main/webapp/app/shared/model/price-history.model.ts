import { IPrice } from 'app/shared/model/price.model';

export interface IPriceHistory {
  id?: number;
  name?: string;
  description?: string | null;
  parent?: IPriceHistory | null;
  child?: IPriceHistory | null;
  price?: IPrice;
}

export const defaultValue: Readonly<IPriceHistory> = {};
