import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBusinessAssociate } from 'app/shared/model/business-associate.model';
import { getEntities as getBusinessAssociates } from 'app/entities/business-associate/business-associate.reducer';
import { getEntity, updateEntity, createEntity, reset } from './provider.reducer';
import { IProvider } from 'app/shared/model/provider.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProviderUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const businessAssociates = useAppSelector(state => state.businessAssociate.entities);
  const providerEntity = useAppSelector(state => state.provider.entity);
  const loading = useAppSelector(state => state.provider.loading);
  const updating = useAppSelector(state => state.provider.updating);
  const updateSuccess = useAppSelector(state => state.provider.updateSuccess);
  const handleClose = () => {
    props.history.push('/provider' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBusinessAssociates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...providerEntity,
      ...values,
      businessAssociate: businessAssociates.find(it => it.id.toString() === values.businessAssociate.toString()),
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
          ...providerEntity,
          businessAssociate: providerEntity?.businessAssociate?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.provider.home.createOrEditLabel" data-cy="ProviderCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.provider.home.createOrEditLabel">Create or edit a Provider</Translate>
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
                  id="provider-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.provider.name')}
                id="provider-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.provider.description')}
                id="provider-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="provider-businessAssociate"
                name="businessAssociate"
                data-cy="businessAssociate"
                label={translate('managerProductStockApp.provider.businessAssociate')}
                type="select"
                required
              >
                <option value="" key="0" />
                {businessAssociates
                  ? businessAssociates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.comercialName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/provider" replace color="info">
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

export default ProviderUpdate;
