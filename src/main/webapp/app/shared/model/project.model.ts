import { IBusinessAssociate } from 'app/shared/model/business-associate.model';
import { IProjectGroup } from 'app/shared/model/project-group.model';
import { ProjectType } from 'app/shared/model/enumerations/project-type.model';

export interface IProject {
  id?: number;
  name?: string;
  type?: ProjectType;
  businessAssociate?: IBusinessAssociate;
  projectGroup?: IProjectGroup;
}

export const defaultValue: Readonly<IProject> = {};
