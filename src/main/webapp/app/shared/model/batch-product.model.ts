import { IProduct } from 'app/shared/model/product.model';
import { IBatch } from 'app/shared/model/batch.model';

export interface IBatchProduct {
  id?: number;
  name?: string;
  description?: string | null;
  product?: IProduct;
  batch?: IBatch;
}

export const defaultValue: Readonly<IBatchProduct> = {};
