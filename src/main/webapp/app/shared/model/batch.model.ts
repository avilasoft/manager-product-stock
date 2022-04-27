import { IBatchStatus } from 'app/shared/model/batch-status.model';
import { IBusinessAssociate } from 'app/shared/model/business-associate.model';

export interface IBatch {
  id?: number;
  name?: string;
  description?: string | null;
  bachStatus?: IBatchStatus;
  businessAssociate?: IBusinessAssociate;
}

export const defaultValue: Readonly<IBatch> = {};
