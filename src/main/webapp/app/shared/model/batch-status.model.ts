import { IBatch } from 'app/shared/model/batch.model';

export interface IBatchStatus {
  id?: number;
  name?: string;
  description?: string | null;
  baches?: IBatch[] | null;
}

export const defaultValue: Readonly<IBatchStatus> = {};
