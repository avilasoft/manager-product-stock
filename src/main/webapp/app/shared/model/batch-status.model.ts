export interface IBatchStatus {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<IBatchStatus> = {};
