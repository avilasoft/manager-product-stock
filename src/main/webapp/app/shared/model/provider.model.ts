import { IUser } from 'app/shared/model/user.model';

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
}

export const defaultValue: Readonly<IProvider> = {};
