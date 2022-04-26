import { IBatch } from 'app/shared/model/batch.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IBatchProduct {
  id?: number;
  name?: string;
  description?: string | null;
  batches?: IBatch[] | null;
  product?: IProduct;
}

export const defaultValue: Readonly<IBatchProduct> = {};
