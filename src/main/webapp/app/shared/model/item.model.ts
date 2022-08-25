import { IProviderItem } from 'app/shared/model/provider-item.model';
import { ItemType } from 'app/shared/model/enumerations/item-type.model';

export interface IItem {
  id?: number;
  name?: string;
  description?: string | null;
  type?: ItemType;
  imageContentType?: string | null;
  image?: string | null;
  providerItems?: IProviderItem[] | null;
}

export const defaultValue: Readonly<IItem> = {};
