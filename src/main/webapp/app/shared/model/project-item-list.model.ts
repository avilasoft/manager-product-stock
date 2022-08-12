import { IUnitPriceList } from 'app/shared/model/unit-price-list.model';
import { IComputedPriceList } from 'app/shared/model/computed-price-list.model';
import { IProject } from 'app/shared/model/project.model';
import { ProjectItemListType } from 'app/shared/model/enumerations/project-item-list-type.model';
import { ProjectItemListStatus } from 'app/shared/model/enumerations/project-item-list-status.model';

export interface IProjectItemList {
  id?: number;
  name?: string;
  description?: string | null;
  type?: ProjectItemListType;
  status?: ProjectItemListStatus;
  unitPriceList?: IUnitPriceList | null;
  computedPriceList?: IComputedPriceList | null;
  project?: IProject;
}

export const defaultValue: Readonly<IProjectItemList> = {};
