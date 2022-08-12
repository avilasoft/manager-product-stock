import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';

export interface IUnitPriceListItem {
  id?: number;
  code?: string;
  name?: string | null;
  unitCostTotal?: number;
  projectItemListItem?: IProjectItemListItem;
  unitPriceList?: IUnitPriceList;
}

export const defaultValue: Readonly<IUnitPriceListItem> = {};
