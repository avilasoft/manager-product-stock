import { IBusinessAssociate } from 'app/shared/model/business-associate.model';

export interface IProvider {
  id?: number;
  name?: string;
  description?: string | null;
  businessAssociate?: IBusinessAssociate;
}

export const defaultValue: Readonly<IProvider> = {};
