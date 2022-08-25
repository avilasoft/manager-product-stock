import { IUnitPriceListItem } from 'app/shared/model/unit-price-list-item.model';
import { IProjectItemList } from 'app/shared/model/project-item-list.model';
import { IItem } from 'app/shared/model/item.model';
import { IProviderItem } from 'app/shared/model/provider-item.model';
import { IUnit } from 'app/shared/model/unit.model';

export interface IProjectItemListItem {
  id?: number;
  code?: string;
  dimension?: string;
  quantity?: number;
  description?: string | null;
  unitPriceListItem?: IUnitPriceListItem | null;
  projectItemList?: IProjectItemList;
  item?: IItem;
  providerItem?: IProviderItem | null;
  unit?: IUnit;
}

export const defaultValue: Readonly<IProjectItemListItem> = {};
