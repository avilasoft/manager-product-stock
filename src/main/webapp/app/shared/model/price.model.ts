import { IPriceHistory } from 'app/shared/model/price-history.model';

export interface IPrice {
  id?: number;
  name?: string;
  description?: string | null;
  priceHistories?: IPriceHistory[] | null;
}

export const defaultValue: Readonly<IPrice> = {};
