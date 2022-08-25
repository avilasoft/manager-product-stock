import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './business-associate.reducer';
import { IBusinessAssociate } from 'app/shared/model/business-associate.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { BusinessAssociateType } from 'app/shared/model/enumerations/business-associate-type.model';

export const BusinessAssociateUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const businessAssociateEntity = useAppSelector(state => state.businessAssociate.entity);
  const loading = useAppSelector(state => state.businessAssociate.loading);
  const updating = useAppSelector(state => state.businessAssociate.updating);
  const updateSuccess = useAppSelector(state => state.businessAssociate.updateSuccess);
  const businessAssociateTypeValues = Object.keys(BusinessAssociateType);
  const handleClose = () => {
    props.history.push('/business-associate' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...businessAssociateEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'PROVIDER',
          ...businessAssociateEntity,
          user: businessAssociateEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.businessAssociate.home.createOrEditLabel" data-cy="BusinessAssociateCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.businessAssociate.home.createOrEditLabel">
              Create or edit a BusinessAssociate
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="business-associate-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.businessAssociate.comercialName')}
                id="business-associate-comercialName"
                name="comercialName"
                data-cy="comercialName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.businessAssociate.description')}
                id="business-associate-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('managerProductStockApp.businessAssociate.type')}
                id="business-associate-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {businessAssociateTypeValues.map(businessAssociateType => (
                  <option value={businessAssociateType} key={businessAssociateType}>
                    {translate('managerProductStockApp.BusinessAssociateType.' + businessAssociateType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="business-associate-user"
                name="user"
                data-cy="user"
                label={translate('managerProductStockApp.businessAssociate.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/business-associate" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BusinessAssociateUpdate;
