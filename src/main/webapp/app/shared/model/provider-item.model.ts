import { IProvider } from 'app/shared/model/provider.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IItem } from 'app/shared/model/item.model';

export interface IProviderItem {
  id?: number;
  code?: string;
  cost?: number;
  description?: string | null;
  provider?: IProvider;
  unit?: IUnit;
  item?: IItem;
}

export const defaultValue: Readonly<IProviderItem> = {};
