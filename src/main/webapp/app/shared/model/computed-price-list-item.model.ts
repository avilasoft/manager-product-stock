import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';
import { IComputedPriceList } from 'app/shared/model/computed-price-list.model';

export interface IComputedPriceListItem {
  id?: number;
  code?: string;
  name?: string | null;
  computedCostTotal?: number;
  computedQuantityTotal?: number;
  unitPriceList?: IUnitPriceList;
  computedPriceList?: IComputedPriceList;
}

export const defaultValue: Readonly<IComputedPriceListItem> = {};
