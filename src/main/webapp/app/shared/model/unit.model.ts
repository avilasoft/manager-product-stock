import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { IProviderItem } from 'app/shared/model/provider-item.model';
import { UnitType } from 'app/shared/model/enumerations/unit-type.model';

export interface IUnit {
  id?: number;
  name?: string;
  description?: string;
  symbol?: string;
  type?: UnitType;
  projectItemListItems?: IProjectItemListItem[] | null;
  providerItems?: IProviderItem[] | null;
}

export const defaultValue: Readonly<IUnit> = {};
