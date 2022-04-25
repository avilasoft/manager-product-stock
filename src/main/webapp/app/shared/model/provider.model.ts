import { IUser } from 'app/shared/model/user.model';
import { IProviderProduct } from 'app/shared/model/provider-product.model';

export interface IProvider {
  id?: number;
  name?: string;
  descrption?: string;
  email?: string;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  city?: string;
  country?: string;
  user?: IUser;
  providerProducts?: IProviderProduct[] | null;
}

export const defaultValue: Readonly<IProvider> = {};
