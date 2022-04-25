import { IBatch } from 'app/shared/model/batch.model';
import { IProviderProduct } from 'app/shared/model/provider-product.model';

export interface IBatchProduct {
  id?: number;
  name?: string;
  description?: string | null;
  batch?: IBatch;
  providerProducts?: IProviderProduct[];
}

export const defaultValue: Readonly<IBatchProduct> = {};
