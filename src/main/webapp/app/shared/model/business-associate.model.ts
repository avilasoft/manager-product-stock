import { IUser } from 'app/shared/model/user.model';
import { IBatch } from 'app/shared/model/batch.model';

export interface IBusinessAssociate {
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
  batches?: IBatch[] | null;
}

export const defaultValue: Readonly<IBusinessAssociate> = {};
