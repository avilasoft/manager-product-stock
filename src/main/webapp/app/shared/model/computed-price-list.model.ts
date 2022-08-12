import { IComputedPriceListItem } from 'app/shared/model/computed-price-list-item.model';
import { IProjectItemList } from 'app/shared/model/project-item-list.model';

export interface IComputedPriceList {
  id?: number;
  code?: string;
  computedPriceListTotal?: number | null;
  computedPriceListItems?: IComputedPriceListItem[] | null;
  projectItemList?: IProjectItemList | null;
}

export const defaultValue: Readonly<IComputedPriceList> = {};
