import React from 'react';

function LibraryButton({ buttonLabel, onClickButton }) {
  const isDeleteButton = buttonLabel.toLowerCase().includes('delete');

  return (
    <span>
      <button
        className={isDeleteButton ? 'delete-button' : ''}
        onClick={onClickButton}
      >
        {buttonLabel}
      </button>
    </span>
  );
}

export default LibraryButton;