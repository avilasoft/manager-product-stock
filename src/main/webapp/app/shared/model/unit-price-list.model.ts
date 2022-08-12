import { IUnitPriceListItem } from 'app/shared/model/unit-price-list-item.model';
import { IComputedPriceListItem } from 'app/shared/model/computed-price-list-item.model';
import { IProjectItemList } from 'app/shared/model/project-item-list.model';

export interface IUnitPriceList {
  id?: number;
  code?: string;
  unitPriceListTotal?: number | null;
  unitPriceListItems?: IUnitPriceListItem[] | null;
  computedPriceListItems?: IComputedPriceListItem[] | null;
  projectItemList?: IProjectItemList | null;
}

export const defaultValue: Readonly<IUnitPriceList> = {};
