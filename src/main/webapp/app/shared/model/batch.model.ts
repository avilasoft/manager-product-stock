import { IBatchStatus } from 'app/shared/model/batch-status.model';
import { IBusinessAssociate } from 'app/shared/model/business-associate.model';
import { IBatchProduct } from 'app/shared/model/batch-product.model';

export interface IBatch {
  id?: number;
  name?: string;
  description?: string | null;
  bachStatus?: IBatchStatus;
  businessAssociate?: IBusinessAssociate;
  batchProducts?: IBatchProduct[] | null;
}

export const defaultValue: Readonly<IBatch> = {};
