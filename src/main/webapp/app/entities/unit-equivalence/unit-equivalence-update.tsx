import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities as getUnitEquivalences } from 'app/entities/unit-equivalence/unit-equivalence.reducer';
import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { getEntity, updateEntity, createEntity, reset } from './unit-equivalence.reducer';
import { IUnitEquivalence } from 'app/shared/model/unit-equivalence.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitEquivalenceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const unitEquivalences = useAppSelector(state => state.unitEquivalence.entities);
  const units = useAppSelector(state => state.unit.entities);
  const unitEquivalenceEntity = useAppSelector(state => state.unitEquivalence.entity);
  const loading = useAppSelector(state => state.unitEquivalence.loading);
  const updating = useAppSelector(state => state.unitEquivalence.updating);
  const updateSuccess = useAppSelector(state => state.unitEquivalence.updateSuccess);
  const handleClose = () => {
    props.history.push('/unit-equivalence' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUnitEquivalences({}));
    dispatch(getUnits({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...unitEquivalenceEntity,
      ...values,
      child: unitEquivalences.find(it => it.id.toString() === values.child.toString()),
      unit: units.find(it => it.id.toString() === values.unit.toString()),
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
          ...unitEquivalenceEntity,
          unit: unitEquivalenceEntity?.unit?.id,
          child: unitEquivalenceEntity?.child?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="managerProductStockApp.unitEquivalence.home.createOrEditLabel" data-cy="UnitEquivalenceCreateUpdateHeading">
            <Translate contentKey="managerProductStockApp.unitEquivalence.home.createOrEditLabel">
              Create or edit a UnitEquivalence
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
                  id="unit-equivalence-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('managerProductStockApp.unitEquivalence.name')}
                id="unit-equivalence-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('managerProductStockApp.unitEquivalence.description')}
                id="unit-equivalence-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="unit-equivalence-unit"
                name="unit"
                data-cy="unit"
                label={translate('managerProductStockApp.unitEquivalence.unit')}
                type="select"
                required
              >
                <option value="" key="0" />
                {units
                  ? units.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="unit-equivalence-child"
                name="child"
                data-cy="child"
                label={translate('managerProductStockApp.unitEquivalence.child')}
                type="select"
              >
                <option value="" key="0" />
                {unitEquivalences
                  ? unitEquivalences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/unit-equivalence" replace color="info">
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

export default UnitEquivalenceUpdate;
