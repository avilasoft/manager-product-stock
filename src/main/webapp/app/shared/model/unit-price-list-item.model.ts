import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';

export interface IUnitPriceListItem {
  id?: number;
  code?: string;
  unitPriceTotal?: number;
  description?: string | null;
  projectItemListItem?: IProjectItemListItem | null;
  unitPriceList?: IUnitPriceList;
}

export const defaultValue: Readonly<IUnitPriceListItem> = {};
